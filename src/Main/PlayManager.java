package Main;

import java.awt.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import Mino.*;

public class PlayManager {

    final int width = 360;
    final int height = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //Mino
    mino currentMino;
    final int Mino_Start_X;
    final int Mino_Start_Y;

    mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>() ;

    public static int dropInterval = 60;


    public PlayManager() {

        left_x = (GamePanel.width/2) - (width/2);
        right_x = left_x + width;
        top_y = 50;
        bottom_y = top_y + height;

        Mino_Start_X = left_x + (width/2) - Block.SIZE;
        Mino_Start_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;


        currentMino = pickMino();
        currentMino.setXY(Mino_Start_X, Mino_Start_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

    }
    private mino pickMino(){
        mino Mino = null;
        int i = new Random().nextInt(7);

        switch(i){
            case 0: Mino = new MinoL1();break;
            case 1: Mino = new MinoL2();break;
            case 2: Mino = new MinoT();break;
            case 3: Mino = new MinoBar();break;
            case 4: Mino = new MinoZ1();break;
            case 5: Mino = new MinoZ2();break;
            case 6: Mino = new MinoSquare();break;
        }
        return Mino;
    }
    public void update() {
        if(currentMino.active == false){

            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            currentMino.deactivating = false;

            currentMino = nextMino;
            currentMino.setXY(Mino_Start_X, Mino_Start_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);


            checkDelete();
        }
        else {
            currentMino.update();
        }
    }
    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;

        while (x < right_x && y < bottom_y) {

            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    blockCount++;
                }
            }

            x += Block.SIZE;

            if (x == right_x) {

                if (blockCount == 12) {
                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        if(staticBlocks.get(i).y < y){
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
    }
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;  // Use Graphics2D for setStroke
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, width + 8, height + 8);


        int x = right_x +100;
        int y = bottom_y -200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawString("Next",x+60,y+60);

        if(currentMino != null) {
            currentMino.draw(g2);
        }

        nextMino.draw(g2);

        for(int i = 0 ; i < staticBlocks.size() ; i++){
            staticBlocks.get(i).draw(g2);
        }

        g2.setColor(Color.red);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("Paused", x, y);
        }

    }
}
