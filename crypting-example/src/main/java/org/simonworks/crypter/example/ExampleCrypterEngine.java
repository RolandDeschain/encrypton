package org.simonworks.crypter.example;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.simonworks.cryptengine.api.AbstractCrypterEngine;
import org.simonworks.cryptengine.api.Replacing;
import org.simonworks.crypter.example.sequences.SingletonSequenceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This engine is intended for cryptinf files in follow format
 * <br/>
 * [A-Z]{6}[0-9]{2}[A-Z]{1}[0-9]{2}[A-Z]{1}[0-9]{3}[A-Z]{1}[0-9]{19}[A-Z]{2}[0-9]{11}[A-C]{1}
 * <br/>
 * <ul>
 * <li>[A-Z]{6}[0-9]{2}[A-Z]{1}[0-9]{2}[A-Z]{1}[0-9]{3}[A-Z]{1} is Italian FiscalCode format </li>
 * <li>[0-9]{19} contains 11 digits for Italian TaxCode format and 8 digits for a validity date in yyyyMMdd format</li>
 * <li>[A-Z]{2} is a code for province</li>
 * <li>[0-9]{11} contains other 8 digits for the date (yyyyMMdd) when data has been taken and 3 digits for age of the user</li>
 * <li>[A-C]{1} contains a control character whose value can be : (A)ctive data, (B)locked for some reason and (C)ancelled for logical deletion.</li>
 * 
 * 
 * @author SimonWorks
 *
 */
public class ExampleCrypterEngine extends AbstractCrypterEngine {
	
	private static final Logger LOG = LoggerFactory.getLogger( ExampleCrypterEngine.class );
	
	public ExampleCrypterEngine() {
		super();
	}

	@Override
	public String crypt(String line) {
		StringBuilder sb = new StringBuilder();
		if( LOG.isTraceEnabled() ) {
			LOG.trace( "Crypting line <{}>", line);
		}
		replace(sb, line, 0, 16, SingletonSequenceFactory.FISCAL_CODE_SEQUENCE);
		replace(sb, line, 16, 27, SingletonSequenceFactory.VAT_SEQUENCE);
		replace(sb, line, 27, 35, SingletonSequenceFactory.VAL_DATE_SEQUENCE);
		replace(sb, line, 35, 37, SingletonSequenceFactory.PROVINCE_SEQUENCE);
		replace(sb, line, 37, 45, SingletonSequenceFactory.INSERT_DATE_SEQUENCE);
		replace(sb, line, 45, 48, SingletonSequenceFactory.AGE_SEQUENCE);
		sb.append( line.substring( 48 ) );
		
		return sb.toString();
	}
	
	protected Replacing replace(StringBuilder sb, String line, int i, int j, SingletonSequenceFactory sequence) {
		Replacing result = new Replacing(line, i, j);
		
		result.setFieldId( sequence.name() );
		
		/**
		 * Se il valore e' vuoto mantengo il valore vuoto anche sul nuovo file
		 */
		if( StringUtils.isBlank(result.getOldValue()) ) {
			result.setNewValue( result.getOldValue() );
		} else {
			this.setNewValue(sequence.get(), result);
		}
		
		super.append(sb, (j - i), result.getNewValue());
		
		return result;
	}

	@Override
	public String getDescription() {
		return "This is a example crypter engine";
	}
	
	@Override
	public List<String> getSequencesNames() {
		return Arrays.asList(
				SingletonSequenceFactory.FISCAL_CODE_SEQUENCE.name(),
				SingletonSequenceFactory.VAT_SEQUENCE.name(),
				SingletonSequenceFactory.VAL_DATE_SEQUENCE.name(),
				SingletonSequenceFactory.PROVINCE_SEQUENCE.name(),
				SingletonSequenceFactory.INSERT_DATE_SEQUENCE.name(),
				SingletonSequenceFactory.AGE_SEQUENCE.name());
	}

	@Override
	public void storeSequencesStatus() {
		LOG.info("storeSequencesStatus() -> Storing values :");
		for(String seqname : this.getSequencesNames()) {
			SingletonSequenceFactory sequenceFactory = Enum.valueOf(SingletonSequenceFactory.class, seqname);
			String next = sequenceFactory.next();
			getCryptCache().update(AbstractCrypterEngine.SEQUENCES_VALUES, seqname, next);
			LOG.info("{}-><{}>", seqname, next);
		}
	}

	@Override
	public void loadSequencesStatus() {
		LOG.info("loadSequencesStatus()");
		for(String seqname : this.getSequencesNames()) {
			String previousValue = StringUtils.EMPTY;
			SingletonSequenceFactory sequenceFactory = Enum.valueOf(SingletonSequenceFactory.class, seqname);
			try {
				previousValue = getCryptCache().recover(AbstractCrypterEngine.SEQUENCES_VALUES, seqname);
				sequenceFactory.synchWithValue(previousValue);
				LOG.info("Increment {}, value to <{}>", seqname, sequenceFactory.next());
			} catch (Exception e) {
				LOG.info("No previous value available for key <{}>", seqname);
				sequenceFactory.next();
			}
		}
	}

}
