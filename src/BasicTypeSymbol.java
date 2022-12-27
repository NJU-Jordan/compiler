public class BasicTypeSymbol extends BaseSymbol implements Type {
    // INT FLOAT DOUBLE 类型符号
    public int identity=0;
    public BasicTypeSymbol(String name) {
        super(name, null);  //名字已经表明类型
    }
    public int getIdentity(){
        return identity;
    }
}
