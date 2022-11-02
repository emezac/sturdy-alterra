import profile from 'app/entities/profile/profile.reducer';
import player from 'app/entities/player/player.reducer';
import pack from 'app/entities/pack/pack.reducer';
import card from 'app/entities/card/card.reducer';
import game from 'app/entities/game/game.reducer';
import gameConfig from 'app/entities/game-config/game-config.reducer';
import map from 'app/entities/map/map.reducer';
import dungeon from 'app/entities/dungeon/dungeon.reducer';
import floor from 'app/entities/floor/floor.reducer';
import floorConfig from 'app/entities/floor-config/floor-config.reducer';
import prize from 'app/entities/prize/prize.reducer';
import roomConfig from 'app/entities/room-config/room-config.reducer';
import room from 'app/entities/room/room.reducer';
import challenge from 'app/entities/challenge/challenge.reducer';
import door from 'app/entities/door/door.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  profile,
  player,
  pack,
  card,
  game,
  gameConfig,
  map,
  dungeon,
  floor,
  floorConfig,
  prize,
  roomConfig,
  room,
  challenge,
  door,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
