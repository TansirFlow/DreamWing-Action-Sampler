public class Vector2D {
    protected double x;

    protected double y;

    public Vector2D(){
        this(0,0);
    }

    public Vector2D(float _x,float _y){
        x=_x;
        y=_y;
    }

    public void update(float _x,float _y){
        x=_x;
        y=_y;
    }
}
