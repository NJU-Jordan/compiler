public class BaseSymbol implements Symbol {
    final String name;
    final Type type;



    public Type getType() {
        return type;
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
