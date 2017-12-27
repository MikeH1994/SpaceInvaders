package utils;

public class SIUtils {
    public final float magnitude(float... args){
        float squareSum = 0;
        for(int i = 0; i<args.length; i++){
            squareSum+=args[i];
        }
        return (float) Math.sqrt(squareSum);
    }
}
