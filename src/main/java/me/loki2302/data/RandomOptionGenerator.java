package me.loki2302.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomOptionGenerator<T> {
    private final Random random = new Random();
    private final List<OptionInfo<T>> options = new ArrayList<OptionInfo<T>>();
    private int probabilitySum = 0;        
    
    public RandomOptionGenerator<T> withOption(T option, int probability) {
        OptionInfo<T> optionInfo = new OptionInfo<T>(
                option,
                probabilitySum,
                probabilitySum + probability);
        probabilitySum += probability;
        options.add(optionInfo);
        return this;
    }
    
    public T generate() {
        int value = random.nextInt(probabilitySum);
        for(OptionInfo<T> optionInfo : options) {
            if(value >= optionInfo.low && value < optionInfo.high) {
                return optionInfo.option;
            }
        }
        
        throw new RuntimeException();
    }
    
    private static class OptionInfo<T> {
        public final T option;
        public final int low;
        public final int high;
        
        public OptionInfo(T option, int low, int high) {
            this.option = option;
            this.low = low;
            this.high = high;
        }
    }
}