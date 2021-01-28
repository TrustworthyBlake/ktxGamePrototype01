package com.github.TrustworthyBlake.ktxGamePrototype01;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Prot01 extends Game {
	@Override
	public void create() {
		setScreen(new FirstScreen());
	}
}