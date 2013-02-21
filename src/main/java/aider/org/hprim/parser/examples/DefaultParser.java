package aider.org.hprim.parser.examples;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import aider.org.hprim.parser.ContentHandler;
import aider.org.hprim.parser.HPRIMSInputStreamReader;
import aider.org.hprim.parser.HPRIMSTokenSource;
import aider.org.hprim.parser.antlr.HPRIMSParser;

/**
 * Classe permettant d'abstraire les taches à effectuer auprès du tokenizer, parser, ...
 * et permettant d'avoir une interface (API) simple
 * @author delabre
 * @version $Revision: 1028 $
 */
public class DefaultParser {

	/**
	 * Le parseur utilisé
	 */
	protected HPRIMSParser parser = null;
	
	/**
	 * Constructeur par défaut protégé utilisé dans les classes dérivant de celle-ci
	 * @throws IOException
	 */
	protected DefaultParser() throws IOException {
	}
	
	/**
	 * Constructeur
	 * @param input Source du flux de caractères
	 * @param collecteur Reçoit les éléments du flux HPRIM
	 */
	public DefaultParser(InputStream input, ContentHandler collecteur) throws IOException {
		// Création de la source des tokens
		HPRIMSInputStreamReader inputreader = new HPRIMSInputStreamReader(input, "ISO8859_1");
		HPRIMSTokenSource toksce = new HPRIMSTokenSource(inputreader);
		// Création du flux de tokens
		TokenStream tokenstream = new CommonTokenStream (toksce);
		// Le plus important, création du parser de tokens provenant de input et réalisant dans
		// la classe collecteur l'export des données
		this.parser = new HPRIMSParser(tokenstream, collecteur);
		// Pour l'instant, seul le parser hprim 2.1 est fonctionnel
	}
		
	/**
	 * Parse le fichier en entier
	 * @return true si le parsing a réussi sans erreurs, false sinon
	 */
	public boolean parse() throws RecognitionException {
		// En fait, le parsing n'envoie pas de RecognitionException, tout est enregistré dans
		// la liste des erreurs
		try {
			parser.hprim();
		} catch (RecognitionException ignore) {
			// Ignore
		} catch (IllegalArgumentException e) {
			if (e.getCause() instanceof RecognitionException)
				throw (RecognitionException)e.getCause();
			else
				throw e;
		}
		return true;
	}
	
}
