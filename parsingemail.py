#!/usr/bin/python
# coding: utf-8
#pip install beautifulsoup4



from email.parser import BytesParser, Parser
from email.policy import default
from email.message import EmailMessage
from bs4 import BeautifulSoup



#parsing et ouverture du fichier

filename = './data/president_2010/president_2010-06/3'
with open(filename, 'rb') as file:
  headers = BytesParser(policy=default).parse(file)





#  affichage du to, from, sujet, cc et date
print('To: {}'.format(headers['to']))
print('From: {}'.format(headers['from']))
print('Subject: {}'.format(headers['subject']))
print('Date: {}'.format(headers['date']))
print('CC: {}'.format(headers['Cc']))


if headers.is_multipart() :
  lien = []
  pj = []
  signature = []
  for part in headers.walk():
    ctype = part.get_content_type()
    cdispo = str(part.get('Content-Disposition'))

    #si c'est du text plain alors j'affiche directement le contenu du mail
    if ctype == 'text/plain' and 'attachment' not in cdispo:

      body = part.get_payload(decode=True)  # decode
      soup = BeautifulSoup(body, "html.parser")
      text = soup.get_text()
      print(text)

    #s'il y'a du html, puisque j'ai deja affiché le contenu du mail grace au text plain, je traite seulement les signatures
    #j'ai en effet remarqué que dans tous les mails il y'a du text plain et du text html
    elif ctype == 'text/html' and 'attachment' not in cdispo:

      body = part.get_payload(decode=True)  # decode
      soup = BeautifulSoup(body, "html.parser")
      for p in soup.find_all('a'):
        lien.append(p.get("href"))

      for v in soup.find_all('pre'):
        signature.append(v)

      print(lien)
      print(signature)

     #sinon s'il y'a des pieces jointes, j'enregistre leurs noms et je l'ai enregistre décodée dans un nouveau fichier qui sera sauvegardé dans mon dossier venv
    elif 'attachment' in cdispo:
      filename = part.get_filename()
      pj.append(filename)
      print(pj)

      fp = open(filename, 'wb')
      fp.write(part.get_payload(decode=True))
      fp.close()


  else:
    body = headers.get_payload(decode=True)



