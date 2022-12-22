public class FunctionSymbol extends BaseScope implements Symbol{
     Type functionType;

    public FunctionSymbol(String name, Type functionType, Scope enclosingScope) {
        super(name,enclosingScope);
        this.functionType=functionType;


    }

    @Override
    public Type getType() {
        return functionType;
    }

    public void setType(Type type){
        this.functionType=type;
    }
}
