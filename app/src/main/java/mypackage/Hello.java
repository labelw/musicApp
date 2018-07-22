package mypackage;

/**
 * Created by WJY on 2017/4/18.
 */

public class Hello {
    String name;
    int imageid;
    Hello( String name,int imageid)
    {
        this.name=name;
        this.imageid=imageid;
    }
    public String getname()
    {return name;}
    public  int getImageid()
    {
        return imageid;
    }
}
