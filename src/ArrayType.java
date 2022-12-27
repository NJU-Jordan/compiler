public class ArrayType implements Type{
    int dimen; //有几个维度
    Type basicType;

    public int identity=1;
    public ArrayType(int dimen,Type basicType){

        this.dimen=dimen;
        this.basicType=basicType;
    }
    public int getIdentity(){
        return identity;
    }
}
