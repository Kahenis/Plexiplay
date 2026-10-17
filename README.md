# Plexiplay

Player léger pour Plex TV.

Lecteur Android TV pour un serveur Plex. Projet personnel, non affilié à Plex Inc. L'interface reprend la logique d'un rail gauche et d'une grille, pas la marque officielle.

Identifiant : `tv.plexiplay.app`. Version 1.3.96.

APK debug signé, installable : dernière version dans les [releases](https://github.com/Kahenis/plexiplay/releases/latest). La version stable de référence reste dans [plexiplay-clone](https://github.com/Kahenis/plexiplay-clone/releases/latest).

Installation, comme Plex-Pix : transférer l'APK sur la TV, ou `adb connect` puis `adb install -r PlexiPlay-0.1.6-debug.apk`.

Pour recompiler :

```
echo "sdk.dir=$ANDROID_HOME" > local.properties
./gradlew :app:assembleDebug
```

Le premier `./gradlew` télécharge Gradle et les bibliothèques (une fois). Les suivants réutilisent le démon et ne recompilent que les fichiers modifiés, comme Plex-Pix.

## 0.1

- Connexion par code à 4 caractères sur https://plex.tv/link
- Connexion identifiant / mot de passe en secours (souvent bloquée si le compte a la 2FA)
- Serveur possédé en priorité, sinon serveur partagé par un ami
- Rail gauche rétractable : titres quand il a le focus, initiale quand il est replié
- Le rail et la grille ne se passent pas le focus avec gauche / droite. Valider une catégorie ouvre la grille. Retour ramène au rail, après avoir quitté saisons et épisodes
- Films, saisons, épisodes
- Paramètres : langue de l'application, audio, sous-titres, format, puissance serveur ou TV, nombre de bandes-annonces, serveur si plusieurs, déconnexion
- Preroll serveur lu avant le film s'il est défini (`CinemaTrailersPrerollID`). Virgule = toute la liste dans l'ordre, point-virgule = un clip au hasard. Ce n'est pas un réglage client
- Bandes-annonces cinéma optionnelles (0 par défaut), après le preroll
- « Quoi de neuf » : un appui de 3 s sur OK sur une miniature de « Continuer à regarder » ou de « Regarder à nouveau » ouvre un popup (Supprimer / Annuler) à côté de la miniature. Supprimer remet le titre à « non vu » sur le serveur Plex (statut et position de lecture) : il disparaît de la rubrique et des autres clients Plex du compte
- Rail gauche réduit : une icône par catalogue (maison, claquette, écran, micro pour les spectacles, dossier sinon) et, pour les noms inconnus, les 3 premiers caractères qui suivent le mot clé (« Films Disney » : claquette + DIS). Les étiquettes disparaissent quand le rail est déployé\n- Lecteur Media3 / ExoPlayer : lecture, pause, avance, retour, audio, sous-titres, infos direct ou transcodé

## Modules

- `core/common` verrous de développement
- `core/plex` API, auth, preroll (testable sans Android)
- `core/player` URLs, registre de codecs, choix direct / transcodage
- `app` interface TV

Pour figer un module : ajouter son id dans `tools/locks.json` > `locked`, puis `python3 tools/check_locks.py --update`. La compilation refuse ensuite toute modification. Pour rouvrir : retirer l'id. `--update` ne recalcule pas un module encore verrouillé.

Verrouillés, parce qu'ils fonctionnent :

- `playback` : file de lecture des films, bonus serveur (`cinemaQueue`), bandes-annonces, planification des prerolls
- `players` : sources ExoPlayer et mpv, URLs de flux, choix direct ou transcodage, codecs de l'appareil

Le reste (menus, fiche, paramètres, connexion) reste modifiable.

## Codecs

Le registre liste h264, hevc, mpeg2, mpeg4, vp8, vp9, av1, vc1, aac, mp3, opus, vorbis, flac, pcm, ac3, eac3, dts, truehd, subrip, ass, pgs, vobsub.

Mode serveur : Plex transcode en HLS. Mode TV : lecture directe si la télé décode, sinon repli serveur. L'extension FFmpeg Media3 peut être branchée pour les codecs libres ; les codecs brevetés ne sont pas redistribués.
