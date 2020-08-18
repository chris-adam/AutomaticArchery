package controler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Categorie;

public class AjouterCategoriePrompt extends JDialog{
	private JTextField nom = new JTextField(), 
			typeBlason = new JTextField(), 
			distance = new JFormattedTextField(NumberFormat.getIntegerInstance());
	private JLabel nomLabel = new JLabel("Nom : ", JLabel.RIGHT), 
			typeBlasonLabel = new JLabel("Blason : ", JLabel.RIGHT), 
			distanceLabel = new JLabel("Distance : ", JLabel.RIGHT);
	private JPanel content = new JPanel(new BorderLayout());
	private boolean sendData = false;
	private KeyListener enterListener = new KeyListener(){
	    public void keyReleased(KeyEvent evt){
	    	if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
				sendData = true;
				setVisible(false);
	    	}
	    }
	    public void keyPressed(KeyEvent evt) {}
	    public void keyTyped(KeyEvent evt) {}
	};
	
	public AjouterCategoriePrompt(JFrame frame, String title, Boolean modal){
		super(frame, title, modal);
	    this.setSize(300, 200);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
		
	    JPanel panData = new JPanel(new GridLayout(4, 2));
	    
	    panData.add(nomLabel);
	    JPanel nomPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    nom.setPreferredSize(new Dimension(100, 25));
	    nomPan.add(nom);
		this.nom.addKeyListener(this.enterListener);
		panData.add(nomPan);
		
	    panData.add(typeBlasonLabel);
	    JPanel typeBlasonPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		typeBlason.setPreferredSize(new Dimension(100, 25));
		typeBlasonPan.add(typeBlason);
		this.typeBlason.addKeyListener(this.enterListener);
		panData.add(typeBlasonPan);
		
	    panData.add(distanceLabel);
	    JPanel distancePan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		distance.setPreferredSize(new Dimension(100, 25));
		distancePan.add(distance);
		this.distance.addKeyListener(this.enterListener);
		panData.add(distancePan);
		
		JPanel panButton = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				sendData = true;
				setVisible(false);
			}
		});
		okButton.addKeyListener(this.enterListener);
		JButton annulerButton = new JButton("Annuler");
		annulerButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				setVisible(false);
			}
		});
		panButton.add(okButton);
		panButton.add(annulerButton);

		this.content.add(panData, BorderLayout.CENTER);
		this.content.add(panButton, BorderLayout.SOUTH);
		
		this.setContentPane(this.content);
	}
	
	public Categorie getCategorie(){
		this.setVisible(true);
		try{
			return sendData ? new Categorie(nom.getText().toUpperCase(), typeBlason.getText().toLowerCase(), 
					Integer.valueOf(distance.getText())) : null;
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "<NumberFormatException>\nErreur d'encodage de la catégorie.\nLa distance doit être un nombre.", 
					"Erreur", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
}