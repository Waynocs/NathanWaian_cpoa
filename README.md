![](https://github.com/Waynocs/NathanWaian_cpoa/blob/master/src/assets/icons/icon.png)

# Business PRO Euro Simulator Deluxe Edition

Pour se connecter à une base de données, créer un fichier `ConnectionInfo.ini` à la racine du projet, et le remplir avec ces informations (à changer selon votre configuration) :
```ini
[Connection]
uri="jdbc:mysql://localhost/table?serverTimezone=UTC"
login=root
pass=password
```

---

Requis|État
:-|:-:
ajout client|✅
édition client|✅
vue de tous les clients|✅
vue d'un client en détail|✅
suppression avec vérification d'un client|✅
ajout catégorie|✅
édition catégorie|✅
vue de toutes les catégorie|✅
vue d'une catégorie en détail|✅
suppression avec vérification d'une catégorie|✅
ajout produit|✅
édition produit|✅
vue de tous les produit|✅
vue d'un produit en détail|✅
suppression avec vérification d'un produit|✅
ajout commande|✅
édition commande|✅
vue de toutes les commandes|✅
vue d'une commande en détail|✅
suppression d'une commande|✅
fenêtre en deux parties|❌ (tout est géré en tant qu'onglets)
pas de suppression en cascade|✅
triage des produits (fait via le tableau)|✅
triage des catégories (fait via le tableau)|✅
triage des clients (fait via le tableau)|✅
triage des commandes (fait via le tableau)|✅
filtrage des produits par : |
• catégorie|✅
• prix max|✅
• prix min|✅
• nom|✅
• description|✅
• visuel|✅
filtrage des commandes par : |
• client|✅
• coût max|✅
• coût min|✅
• date max|✅
• date min|✅
• produits|✅
filtrage des catégories par : |
• titre|✅
• visuel|✅
filtrage des clients par : |
• tous les champs (sauf mots de passe)|✅
nombre de produits commandés (dans le détail)|✅
bouton "voir commandes" à partir d'un client|❌
passage d'un mode DAO à l'autre|✅

### [UI](https://github.com/Waynocs/NathanWaian_cpoa/projects/3)

Contributeur|Nombre de tâches|Pourcentage
:-|-:|-:
[Nathan HANEN](https://github.com/WildGoat07)|7|58%
[Waïan SERANO](https://github.com/Waynocs)|5|42%

### [Tests unitaires](https://github.com/Waynocs/NathanWaian_cpoa/projects/2)

Contributeur|Nombre de tâches|Pourcentage
:-|-:|-:
[Nathan HANEN](https://github.com/WildGoat07)|3|60%
[Waïan SERANO](https://github.com/Waynocs)|2|40%

### [DAO/interface console](https://github.com/Waynocs/NathanWaian_cpoa/projects/1)

Contributeur|Nombre de tâches|Pourcentage
:-|-:|-:
[Nathan HANEN](https://github.com/WildGoat07)|15|60%
[Waïan SERANO](https://github.com/Waynocs)|10|40%

### Investissement global

Contributeur|Pourcentage
:-|-:
[Nathan HANEN](https://github.com/WildGoat07)|50%
[Waïan SERANO](https://github.com/Waynocs)|50%
