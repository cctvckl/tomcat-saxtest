package  mypackage;


import java.util.ArrayList;
import java.util.List;

public class Foo {
    private String  name;
    private List<Bar> bars = new ArrayList<>();

    public void addBar(Bar bar){
        bars.add(bar);
    }




    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Foo{");
        sb.append("name='").append(name).append('\'');
        sb.append(", bars=").append(bars);
        sb.append('}');
        return sb.toString();
    }
}