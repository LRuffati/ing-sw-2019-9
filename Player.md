# Player

  - Bool StartingPlayerMarker
  - Pawn
  - Logger ActionHeroComment //seems fun
  - PlayerBoardSide* (* = 1 or 2)
  - ActionTile*
  - AmmoBox
  - Points
  - PowerUps
  - NumOfDeaths
  - Collection<> DamagePoints
  - Weapon [(Weapon, Bool)]
  - Marks = Dict<Player, Integer> //non sono sicuro vada in player

## LeaderBoard

  Collection<>: (Player, Number)* Skull* //regex
