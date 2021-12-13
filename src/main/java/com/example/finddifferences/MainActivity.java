package com.example.finddifferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawView = new DrawView(this);
        setContentView(drawView);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.hint:
                drawView.getGame().showHint();
                drawView.invalidate();
                return true;
        }

        return false;
    }
}

class DrawView extends View {

    static Game game = new Game();
    Paint paint = new Paint();
    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

    float FAULT_RADIUS = 60;
    float HALF_SCREEN_HEIGHT = (displayMetrics.heightPixels - 200) / 2;

    public DrawView(Context context){
        super(context);
        game.initPositionsDifferences();
    }

    @Override
    protected void onDraw(Canvas canvas){

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.find);
        canvas.drawBitmap(bitmap, null, new Rect(0,0, displayMetrics.widthPixels, displayMetrics.heightPixels - 200), paint);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for (Rect position : game.getPositionsDifferences() ){
            if(position.right == 1){
                canvas.drawCircle(position.left, position.top, FAULT_RADIUS, paint);
                canvas.drawCircle(position.left, position.top - HALF_SCREEN_HEIGHT, FAULT_RADIUS, paint);
            }
        }

        if(game.isEnd()){
            game.info = "Конец игры";
        }

        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(game.info, 20, HALF_SCREEN_HEIGHT,paint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();

        for (Rect position : game.getPositionsDifferences() ) {
            if(Math.abs(position.left - X) < FAULT_RADIUS &&
                    (Math.abs(position.top - Y) < FAULT_RADIUS || Math.abs((position.top - HALF_SCREEN_HEIGHT) - Y) < FAULT_RADIUS)){
                game.markPositionDifferences(position);
                invalidate();
            }
        }

        if(game.isGameOver()){
            invalidate();
        }

        return true;
    }

    public Game getGame(){
        return game;
    }

}

class Game{
    public String info = "";
    private List<Rect> positionsDifferences = new LinkedList<>();
    private List<String> hints = new LinkedList<>();
    private boolean end = false;

    public void initPositionsDifferences(){
        positionsDifferences.add(new Rect(670,1267,0,0));
        hints.add("Взгляните на козу");
        positionsDifferences.add(new Rect(930,1378,0,0));
        hints.add("Взгляните на корову");
        positionsDifferences.add(new Rect(402,1615,0,0));
        hints.add("Взгляните на дерево");
        positionsDifferences.add(new Rect(131,1262,0,0));
        hints.add("Взгляните на свинку");
    }

    public boolean isGameOver(){
        for (Rect position: positionsDifferences) {
          if(position.right != 1){
              return false;
          }
        }
        end = true;
        return true;
    }

    public void showHint(){
        for (Rect position: positionsDifferences) {
            if(position.right != 1){
                info = hints.get(positionsDifferences.indexOf(position));
                break;
            }
        }
        return;
    }

    public List<Rect> getPositionsDifferences() {
        return positionsDifferences;
    }

    public void setPositionsDifferences(List<Rect> positionsDifferences) {
        this.positionsDifferences = positionsDifferences;
    }

    public void markPositionDifferences(Rect position){
        positionsDifferences.get(positionsDifferences.indexOf(position)).right = 1;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}