import java.util.ArrayList;

public class FunctionType implements Type{

        Type retTy;
        ArrayList<Type> paramsType;

        public FunctionType(Type retTy,ArrayList<Type> paramsType){
                this.retTy=retTy;
                this.paramsType=paramsType;
        }



}
