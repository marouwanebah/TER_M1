


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Vue extends  JFrame {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private parseur parser ; 
	private JTextArea textMessage = new JTextArea(); 
	
	public Vue() {
	    this.setTitle("Parseur & classificateur");
	    this.setSize(900, 900);

	    
	    
	    JLabel MessageBienvenu = new JLabel("Bienvenue ");
	    JButton boutonOuvrir = new JButton("Selectionner un fichier  MIME");

		GridLayout _layout1 = new GridLayout(1, 2);
			
		JPanel nord  = new JPanel();
		nord .setLayout(_layout1);
		nord .add(MessageBienvenu); 
		nord .add(boutonOuvrir);

		JLabel ChoixAlgo = new JLabel("Choix de l'algorithme");
		JComboBox<String> combo = new JComboBox<String>();
		    combo.addItem("Wor2vec");
		    combo.addItem("K-mens");
		    combo.addItem("H-clustering");
		

		   
		JPanel center = new JPanel();
	    center.setBackground(Color.white);
	    center.setLayout(new FlowLayout());
	    	center.add(this.textMessage);

	    	
		
		JButton bouton = new JButton("Lancer");
		JButton bouton2 = new JButton("Swest");
		bouton2.setEnabled(false);   
	   
		JPanel south = new JPanel();
	    south.add(bouton);
	    south.add(bouton2);
	    
	    

	    
	    JPanel west = new JPanel();
	    west.add(ChoixAlgo);
	    west.add(combo); 

	   // center.add(west, BorderLayout.NORTH);
	    
	    GridLayout _layMain = new GridLayout(2,2); 
	    JPanel mainPanel = new JPanel(); 
	    mainPanel.setLayout(new BorderLayout());
	    
	    JScrollPane scroll = new JScrollPane(textMessage);
	    //this.getContentPane()
	    
	    mainPanel.add(nord , BorderLayout.NORTH);
	    mainPanel.add(west, BorderLayout.WEST);
	    mainPanel.add(scroll, BorderLayout.CENTER);
	    mainPanel.add(south, BorderLayout.SOUTH);
	    

	    
	    this.setContentPane(mainPanel);
	    this.setVisible(true);  
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    
	    boutonOuvrir.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("tts");
		
					selectFile();
	
			}
	    });

	}
	public void selectFile()  {
	    JFileChooser chooser = new JFileChooser();
	    // optionally set chooser options ...
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        File f = chooser.getSelectedFile();
	        
	        try {
				parser = new parseur(f.getAbsolutePath());
				
				
				textMessage.setText(parser.contenuPourGraphique());
				
			} catch (FileNotFoundException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } else {
	        // user changed their mind
	    }
}

}
