<br />
<div align="center">
  <a href="https://github.com/hoangndst/bomb#readme">
    <img src="core/assets/img/logo.gif" alt="icon" width="450" height="200">
  </a>

  <h3 align="center"><strong>Super Bomberman</strong></h3>

  <p align="center">
    Game Project. OOP Project. <a href="https://uet.vnu.edu.vn/"><strong>UET-VNU</strong></a>
    <br />
    <a href="https://github.com/hoangndst/bomb#readme"><strong>Explore the docs</strong></a>
    <br />
    <a href="https://github.com/hoangndst/bomb#readme">View Demo</a>
    ·
    <a href="https://github.com/hoangndst/bomb/issues">Report Bug</a>
</div>


<details>
  <summary><strong>Table of Contents</strong></summary>
  <ol>
    <li>
      <a href="#introduction">Introduction</a>
       <ul>
        <li><a href="#bomberman">Bomberman</a></li>
        <li><a href="#bomb">Bomb</a></li>
        <li><a href="#balloom">Balloom</a></li>
        <li><a href="#bulb">Bulb</a></li>
      </ul>
    </li>
    <li>
      <a href="#game-play">Game Play</a>
    </li>
    <li><a href="#how-to-play">How to play</a></li>
    <li><a href="#libraries">Libraries</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>
<br />

## Introduction

Bomberman is a legendary game that has been around for a long time. It is a game where you control a character called a Bomberman and you must destroy all the enemy that are scattered around the map. The enemy are scattered around the map and you must destroy them in order to win the game.

We created this game within 4 days so the code is not optimized. We will fix it soon when we have time, if we're lazy, it's probably never :)))

### Bomberman
![Bomberman](core/assets/bomberman.png)

Bomberman is a character that you control. You can move it around the map and you can use the bombs to destroy all the bombs that are scattered around the map.
### Bomb
![Bomb](core/assets/bomb.png)

![Flame](core/assets/flame.png)

- Bomb is a special item that you can use to destroy all the bombs that are scattered around the map.

### Balloom
![Balloon](core/assets/balloom.png)

- Balloom is an enemy that you can destroy to win the game. They move with random direction and they are destroyed by the bombs.

### Bulb
![Bulb](core/assets/bulb.png)

- Bulb is an enemy that you can destroy to win the game. They can find the shortest path to the Bomberman and they are destroyed by the bombs.
- The find path algorithm is A* algorithm.

## How to play
- Movement:
  - WASD
- Place a bomb:
  - Space
- Pause the game:
  - Esc
- Show b2d debug(for developers):
  - B

## Game play

![GUI Demo](core/assets/img/map1.png)
![GUI Demo](core/assets/img/map2.png)


## Libraries
- [Libgdx](https://libgdx.badlogicgames.com/)
- [Gdx-ai](https://github.com/libgdx/gdx-ai)

## License
[MIT](https://choosealicense.com/licenses/mit/)
