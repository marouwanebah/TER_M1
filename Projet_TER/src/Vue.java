


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Vue extends  JFrame {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<MailList> liste_mail= new ArrayList<MailList>(); 
	

	private JTextArea textMessage = new JTextArea(); 
	private JButton bouton ;
	private JButton bouton2 ; 
	
	public Vue() {
	    this.setTitle("Parsseur");
	    this.setSize(900, 300);

	    
	    
	    JLabel MessageBienvenu = new JLabel("Bienvenue");
	    JButton boutonOuvrir = new JButton("Sélectionner dossier");

		GridLayout _layout1 = new GridLayout(1, 2);
			
		JPanel nord  = new JPanel();
		nord .setLayout(_layout1);
		nord .add(MessageBienvenu); 
		nord .add(boutonOuvrir);

		
		JLabel ChoixAlgo = new JLabel("Type d'Exportation");
		JComboBox<String> combo = new JComboBox<String>();
		    combo.addItem("Complet");
		    combo.addItem("Contenu Propre + Sujet");
		    combo.addItem("Autres");
		

		   
		JPanel center = new JPanel();
	    center.setBackground(Color.white);
	    center.setLayout(new FlowLayout());
	    	center.add(this.textMessage);

		bouton = new JButton("Exporter en txt");
		bouton2 = new JButton("Exporter en XML"); 
		bouton.setEnabled(false);
		bouton2.setEnabled(false);
		
		JPanel south = new JPanel();
	    south.add(bouton);
	    south.add(bouton2);
	    
	    JPanel west = new JPanel();
	    west.add(ChoixAlgo);
	    west.add(combo); 

	   
	    center.add(west, BorderLayout.NORTH);
	    
	  //  GridLayout _layMain = new GridLayout(2,2); 
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
		
					try {
						selectFile();
					} catch (MessagingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	
			}
	    });
		bouton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
						String text="";
						FileWriter myWriter;
					try {
						if(combo.getSelectedItem()=="Contenu Propre + Sujet") {
							myWriter = new FileWriter("Resultat_sujet_contenu_propre.txt");
							for (MailList a : liste_mail) { 
								text +=a.getSujet()+"\n"+ a.getContenuePropre(); 
								myWriter.write(a.getSujet()+"\n"+ a.getContenuePropre());
							}
							myWriter.close();
							textMessage.setText("Le fichier Resultat_sujet_contenu_propre.txt a été créé\n\n"+text);
						}
						else if (combo.getSelectedItem()=="Complet"){
							myWriter = new FileWriter("Resultat_completet.txt");
							for (MailList a : liste_mail) { 
								text += a.getIdMail()+"\n"+ a.getDate() +"\n"+ a.getTypeemail()+"\n"+
										a.getFrom()+"\n"+ a.getDestinataire()+"\n"+ a.getSujet()+"\n"+
										a.getDestinataireEnCopie() +"\n"+ a.getBody() +"\n"+
										a.getSignature()+"\n"+ a.getLiens() +"\n"+ a.getAttachments()
										+"\n"+a.getContenuePropre();
								myWriter.write(a.getIdMail()+"\n"+ a.getDate() +"\n"+ a.getTypeemail()+"\n"+
								a.getFrom()+"\n"+ a.getDestinataire()+"\n"+ a.getSujet()+"\n"+
								a.getDestinataireEnCopie() +"\n"+ a.getBody() +"\n"+
								a.getSignature()+"\n"+ a.getLiens() +"\n"+ a.getAttachments()
								+"\n"+a.getContenuePropre());
							}
							myWriter.close();
							textMessage.setText("Le fichier Resultat_completet.txt a été créé \n\n"+text);
						}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				      System.out.println("Successfully wrote to the file.");
			}
		});
		bouton2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
	
			      StreamResult result;
			      try {
				    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				    //root elements
				    Document doc = docBuilder.newDocument();
				    Element rootElement = doc.createElement("Email");
				    doc.appendChild(rootElement);
				
					if(combo.getSelectedItem()=="Contenu Propre + Sujet") {
						 result =  new StreamResult(new File("Resultat_sujet_contenu_propre.xml"));
						for (MailList a : liste_mail) { 

						    Element staff = doc.createElement("Sujet");
						    if(a.getSujet()!=null) {
						    	staff.appendChild(doc.createTextNode(a.getSujet()));
						    }
						    rootElement.appendChild(staff);
						    
						    Element contenu = doc.createElement("contenuPropre");
						    if(a.getContenuePropre()!=null) {
						    	contenu.appendChild(doc.createTextNode(a.getContenuePropre()));
						    }
						    rootElement.appendChild(contenu);

						    //write the content into xml file
						    TransformerFactory transformerFactory =  TransformerFactory.newInstance();
						    Transformer transformer = transformerFactory.newTransformer();
						    DOMSource source = new DOMSource(doc);
						    
							
							transformer.transform(source, result);
						}
						 System.out.println("Successfully wrote to the file. cosuj");
						textMessage.setText("Le fichier Resultat_sujet_contenu_propre.xml a été créé ");

					}
					else if (combo.getSelectedItem()=="Complet"){
						 result =  new StreamResult(new File("Resultat_complet.xml"));
						for (MailList a : liste_mail) { 
							
						    //id
						    Element id = doc.createElement("Id");
						    id.appendChild(doc.createTextNode(a.getIdMail()));
						    rootElement.appendChild(id);
						    
						    //type
						    Element type = doc.createElement("Type");
						    type.appendChild(doc.createTextNode(a.getTypeemail()));
						    rootElement.appendChild(type);
						    
						    //date
						    Element date = doc.createElement("Date");
						    date.appendChild(doc.createTextNode(a.getDate()));
						    rootElement.appendChild(date);	
						    
						    //Expédtieur
						    Element expediteur = doc.createElement("Expediteur");
						    if(a.getFrom() !=null) {
						    	expediteur.appendChild(doc.createTextNode(a.getFrom().getEmailPersonne()));
							}
						    rootElement.appendChild(expediteur);
						    
						    //Destinataire
						    Element Destinataire = doc.createElement("Destinataire");
						    if(a.getFrom() !=null) {
						    	Destinataire.appendChild(doc.createTextNode(a.getDestinataire().toString()));
						    }
						    rootElement.appendChild(Destinataire);

						    //Destinataire en copie
						    Element DestinataireCopie = doc.createElement("DestinataireCopie");
						    	if(a.getDestinataireEnCopie() !=null) {	   
						    		DestinataireCopie.appendChild(doc.createTextNode(a.getDestinataireEnCopie().toString()));   
						    	}	
						    rootElement.appendChild(DestinataireCopie);
							
						    //sujet
						    Element sujet = doc.createElement("Sujet");
						    sujet.appendChild(doc.createTextNode(a.getSujet()));
						    rootElement.appendChild(sujet);
						   
						    //contenur Brut 
						    Element contenuBrut  = doc.createElement("ContenuBrut");
						    contenuBrut.appendChild(doc.createTextNode(a.getBody()));
						    rootElement.appendChild(contenuBrut);
						    //Signature 
						    Element signature  = doc.createElement("Signature");
						    if( a.getSignature()!=null) {
						    	signature.appendChild(doc.createTextNode(a.getSignature()));
						    }
						    rootElement.appendChild(signature);
						    //attachement 
						    Element attachement  = doc.createElement("Attachements");
						    	if(a.getAttachments()!=null) {
						    		attachement.appendChild(doc.createTextNode(a.getAttachments().toString()));	   
						    	}
						    rootElement.appendChild(attachement);
						    
						    Element contenu = doc.createElement("contenuePropre");
						    contenu.appendChild(doc.createTextNode(a.getContenuePropre()));
						    rootElement.appendChild(contenu);
						    //lien 
						    Element lien  = doc.createElement("lien");
						    	if(a.getLiens()!=null) {
							    
						    		lien.appendChild(doc.createTextNode(a.getLiens().toString()));
						    	}
						    rootElement.appendChild(lien);
						    
						    //write the content into xml file
						    TransformerFactory transformerFactory =  TransformerFactory.newInstance();
						    Transformer transformer = transformerFactory.newTransformer();
						    DOMSource source = new DOMSource(doc);
						    transformer.transform(source, result);
						}
						 System.out.println("Successfully wrote to the file. complete");
						textMessage.setText("Le fichier Resultat_complet.txt a été créé ");
					}
			      }	catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TransformerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});		
	}

	public void selectFile() throws MessagingException, IOException  {
	    JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int i=0, nombre_de_dossier=0; 
	    // optionally set chooser options ...
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        File f = chooser.getSelectedFile();
			File[] files = new File(f.getAbsolutePath()).listFiles();
			System.out.println("============Debut Parsage==============");
			textMessage.setText("Parssage en cours... ");
				for (File file : files) { 
					if(file.isDirectory()) {
						++nombre_de_dossier;
						for(File fileInsideFolder : file.listFiles()) {
								//System.out.println(fileInsideFolder.getAbsolutePath());
								
								parseur test = new parseur(fileInsideFolder.getAbsolutePath());
								MailList a= test.mailToObject(); //
								liste_mail.add(a); 
								++i;
							} 
						}
					else {
						parseur test = new parseur(file.getAbsolutePath());
						MailList a= test.mailToObject(); //
						liste_mail.add(a); 
						textMessage.setText("Début parssage ");
						++i;
					}
				} 

				
				textMessage.setText("Fin Parssage \n Nombre de Dossier : "+nombre_de_dossier+"\n Nombres d'e-mails  parsser : "+i);
				bouton.setEnabled(true);
				bouton2.setEnabled(true);

	    } else {
	    	textMessage.setText(" Pas de dossier selectionné");
	        // user changed their mind
	    }
	}

}
