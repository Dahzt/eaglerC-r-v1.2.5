package argo.jdom;

import argo.saj.InvalidSyntaxException;
import argo.saj.SajParser;
import net.PeytonPlayz585.io.Reader;
import net.PeytonPlayz585.io.StringReader;

import java.io.IOException;

public final class JdomParser {
	public JsonRootNode parse(Reader var1) throws InvalidSyntaxException, IOException {
		JsonListenerToJdomAdapter var2 = new JsonListenerToJdomAdapter();
		(new SajParser()).parse(var1, var2);
		return var2.getDocument();
	}

	public JsonRootNode parse(String var1) throws InvalidSyntaxException {
		try {
			JsonRootNode var2 = this.parse((Reader)(new StringReader(var1)));
			return var2;
		} catch (IOException var4) {
			throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", var4);
		}
	}
}
