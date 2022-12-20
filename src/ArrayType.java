public class ArrayType implements Type{
    int dimen; //有几个维度
    Type basicType;

    public ArrayType(int dimen,Type basicType){

        this.dimen=dimen;
        this.basicType=basicType;
    }

}
