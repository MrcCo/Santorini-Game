package etf.santorini.sm160425d.GUI;

import etf.santorini.sm160425d.Logic.Token;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TokenGUI extends Circle {

    private Token myToken;
    private Color color;

    public TokenGUI(Token token){
        super(75, 75, 20);
        this.myToken = token;

        if(myToken.getPlayer() == 0)
            this.color = Color.BLUE;                                                                                    //player one is blue
        else                                                                                                            //player two is red
            this.color = Color.RED;

        this.setFill(color);
        this.setStroke(Color.BLACK);

    }

    public void highlight(){
        if(myToken.getPlayer() == 0)
            this.color = Color.DARKBLUE;
        else
            this.color = Color.DARKRED;
        this.setFill(color);
    }

    public void lowlight(){
        if(myToken.getPlayer() == 0)
            this.color = Color.BLUE;
        else
            this.color = Color.RED;
        this.setFill(color);

    }

}
