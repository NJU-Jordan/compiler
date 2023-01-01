import org.bytedeco.llvm.LLVM.LLVMValueRef;

import java.util.Map;

public interface Scope {
    public String getName();
    public void setName(String name);
    public Scope getEnclosingScope();
    public Map<String,LLVMValueRef> getSymbols();
    public void define(String name,LLVMValueRef valueRef);

    public LLVMValueRef resolve(String name);
}
