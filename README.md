# MovieClubBackEnd
SpringBoot backend for the MovieClub app
priorità:
- Gestire serie TV (forse fare la classe padre content e figlie movie e TVshow). TVshow ha in piu una lista di stagioni, che una lista di episodi all'interno.
Quindi tre classi: 
- content con titolo, descrizione,Lista di generi, totpoints, votes, (Lazy) List<Ratings>, num_playlist_added_in
- Movie figlia di content con in piu anno, regista 
- Tvshow figlia di content, che contiene lista di stagioni, 
- Stagione che contiene lista di episodi 
- episodi
