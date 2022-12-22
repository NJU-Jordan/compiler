public class BaseSymbol implements Symbol {
     String name;
     Type type;



    public Type getType() {
        return type;
    }
    public void setType(Type type){
        this.type=type;
    }

    public BaseSymbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }
}
