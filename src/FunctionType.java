import java.util.ArrayList;

public class FunctionType implements Type{

         Type retTy;


        ArrayList<Type> paramsType;

        public FunctionType(Type retTy,ArrayList<Type> paramsType){
                this.retTy=retTy;
                this.paramsType=paramsType;
        }

        public void setParamsType(ArrayList<Type> paramsType){
                this.paramsType=paramsType;
        }
        public void setRetTy(Type retTy) {
                this.retTy = retTy;
        }

        public Type getRetTy() {
                return retTy;
        }
}
