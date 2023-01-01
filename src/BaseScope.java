import org.bytedeco.llvm.LLVM.LLVMValueRef;
import org.bytedeco.llvm.global.LLVM;

import java.util.HashMap;
import java.util.Map;

public class BaseScope implements Scope {
    private final Scope enclosingScope;
    private final Map<String, LLVMValueRef> symbols= new HashMap<>();
    private String name;

    public BaseScope(String name, Scope enclosingScope){
        this.name=name;
        this.enclosingScope = enclosingScope;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Scope getEnclosingScope() {
        return this.enclosingScope;
    }

    @Override
    public Map<String, LLVMValueRef> getSymbols() {
        return this.symbols;
    }

    @Override
    public void define(String name,LLVMValueRef valueRef) {
            symbols.put(name,valueRef);
    }

    @Override
    public LLVMValueRef resolve(String name) {
        LLVMValueRef valueRef = symbols.get(name);
        if(valueRef!=null){
            return valueRef;
        }
        if(enclosingScope!=null){
            return enclosingScope.resolve(name);
        }
        return null;
    }
}
