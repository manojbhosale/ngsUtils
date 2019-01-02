package com.psl.swingexample;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

public class MainScreen extends JFrame {

//declare variables.
JFrame frame;

JLabel welcomeNote,welcomeNote1,loginNote,loginName,password,previlage;
JTextField logintxt;
JPasswordField passwordtxt;
JComboBox previlagecb;

//Constructor
public MainScreen() {

frame = new JFrame("MainScreen");

JPanel panel= new JPanel();
Container content = frame.getContentPane();
content.add(panel);
panel.setLayout(new BorderLayout());

//create welcome panel.
String name = "Welcome to " +"\n" +"Management Information System ";
String name1 ="of Abak L.G.A"; 
JPanel welcomePanel =new JPanel(new GridLayout(0,1,0,5));
welcomeNote = new JLabel(name);
welcomeNote1 = new JLabel(name1);
welcomeNote.setFont(new Font("Arial",Font.BOLD,22));
welcomeNote1.setFont(new Font("Arial",Font.BOLD,22));
welcomePanel.add(welcomeNote);
welcomePanel.add(welcomeNote1);

//create login panel.
JPanel loginPanel = new JPanel(new GridLayout(0,1,0,5));
loginNote = new JLabel("Login according to your previlage");
loginName = new JLabel("Login Name:");
password = new JLabel("Password:");
previlage = new JLabel("Previlage:");
logintxt = new JTextField(10);
passwordtxt = new JPasswordField(10);
String[] previlageName ={"Management","Employee","Adiminstrator"};
previlagecb =new JComboBox(previlageName);

//create a panel to add login information.
JPanel loginInfoPanel = new JPanel(new GridLayout(0,2,0,5));
loginInfoPanel.add(loginName);
loginInfoPanel.add(logintxt);
loginInfoPanel.add(password);
loginInfoPanel.add(passwordtxt);
loginInfoPanel.add(previlage);
loginInfoPanel.add(previlagecb);

//add every component to the loginPanel.
loginPanel.add(loginNote);
loginPanel.add(loginInfoPanel);

//add welcomePanel and loginPanel to panel.
panel.add(welcomePanel,BorderLayout.NORTH);
panel.add(loginPanel,BorderLayout.CENTER);

//frame.setSize(300,300);

frame.setVisible(true);
}



public static void main(String args[]) {
MainScreen ms =new MainScreen();

}
} 