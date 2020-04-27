#!/usr/bin/python
# coding: utf-8
#pip install beautifulsoup4

from email.parser import BytesParser, Parser
from email.policy import default
from email.message import EmailMessage
from bs4 import BeautifulSoup


#ouverture du fichier et parsing du mail avec la fonction bytesparser et parse
#si vous voulez compiler un autre fichier, changer juste le path dans la variable filename
#ceci n'est pas le code final, il sera amelioré par la suite

filename = './data/president_2010/president_2010-06/1'
with open(filename, 'rb') as file:
  headers = BytesParser(policy=default).parse(file)


#affichage des elements souhaités (le destinataire,la date, le sujet, cc...etc)

print('To: {}'.format(headers['to']))
print('From: {}'.format(headers['from']))
print('Subject: {}'.format(headers['subject']))
print('Date: {}'.format(headers['date']))
print('CC: {}'.format(headers['Cc']))


#recuperation du contenu du mail, avec parsing des elements html pour donner un rendu propre du mail
if headers.is_multipart() :
  for part in headers.walk():
    ctype = part.get_content_type()
    cdispo = str(part.get('Content-Disposition'))

    # skip any text/plain (txt) attachments
    if ctype == 'text/plain' and 'attachment' not in cdispo:
      body = part.get_payload(decode=True)  # decode
      soup=BeautifulSoup(body, "html.parser")
      text=soup.get_text()
      print (text)
      break
  else:
    body = b.get_payload(decode=True)
    print (body)


