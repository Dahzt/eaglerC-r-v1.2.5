package argo.jdom;

public final class JsonStringNodeBuilder implements JsonNodeBuilder {
	private final String field_27244_a;

	JsonStringNodeBuilder(String var1) {
		this.field_27244_a = var1;
	}

	public JsonStringNode func_27243_a() {
		return JsonNodeFactories.aJsonString(this.field_27244_a);
	}

	public JsonNode buildNode() {
		return this.func_27243_a();
	}
}
