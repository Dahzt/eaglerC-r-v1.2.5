package net.PeytonPlayz585.awt.datatransfer;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class Clipboard {
	
	String name;
	
	public Clipboard(String name) {
        this.name = name;
    }
	
	public String getName() {
        return name;
    }
	
	@JSBody(params = { "text" }, script = "if(window.navigator.clipboard) window.navigator.clipboard.writeText(text);")
	public native synchronized void setContents(String text);
	
	public synchronized String getContents() {
        return getClipboardContents();
    }
	
	@Async
	public static native String getClipboardContents();
	
	@JSFunctor
	private static interface FunctionResolveString extends JSObject {
		void resolveStr(String s);
	}
	
	@JSBody(params = { "cb" }, script = "if(!window.navigator.clipboard) cb(null); else window.navigator.clipboard.readText().then(function(s) { cb(s); }, function(s) { cb(null); });")
	private static native void getClipboard0(FunctionResolveString cb);
	
	private static void getClipboardContents(final AsyncCallback<String> cb) {
		final long start = System.currentTimeMillis();
		getClipboard0(new FunctionResolveString() {
			@Override
			public void resolveStr(String s) {
				cb.complete(s);
			}
		});
	}
	
	

}
