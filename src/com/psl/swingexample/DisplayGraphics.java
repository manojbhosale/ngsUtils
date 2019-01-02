package com.psl.swingexample;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class DisplayGraphics extends Canvas {
	
	public void paint(Graphics g){
		
		g.drawString("Hello", 40, 40);
		setBackground(Color.WHITE);
		g.fillRect(130, 30, 100, 80);
		g.drawOval(30, 130, 50, 60);
		setForeground(Color.RED);
		//g.fillOval(130, 130, 50, 60);
		g.drawArc(30, 200, 40, 50, 90, 60);
		g.fillArc(30, 200, 40, 50, 90, 60);
		char[] t = {'*','*'};
		g.drawChars(t, 0, 2, 100, 100);
	}
	
	
	public static void main(String args[]){
		
		DisplayGraphics dg = new DisplayGraphics();
		JFrame jf  = new JFrame();
		jf.add(dg);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}

}
