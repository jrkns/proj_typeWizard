package entity.monster;

import input.InputUtility;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import entity.Hero;
import entity.Land;
import entity.skill.FireSkill;
import entity.skill.IceSkill;
import entity.skill.MeteorSkill;
import entity.skill.PoisonSkill;
import entity.skill.Skill;
import entity.skill.SpikeSkill;
import render.Renderable;
import render.Resource;

public class Yeti extends Monster implements Renderable {

	private int countAttack = 0, frameCountAttack = 0, attackDirection,
			speedDecrease = 0;
	private boolean isAttack = false;
	private int[] debuff = new int[3];
	private int[] countDebuff = new int[3];
	private int[] timeCountDebuff = new int[3];

	public Yeti(int x, int y, Land land, Hero hero) {
		this.x = x;
		this.y = y;
		this.land = land;
		this.hero = hero;
		level = (int) (Math.random() * 5) + 1; // monster has level.
		hp = 200 * level;// level involve to hp.
		attack = 20 * level;// level involve to attack.
		type = 2; // Ice Monster Type.

	}

	@Override
	public void update() {
		if (hp == 0) { // dead
			if (countDead == 15) {
				countDead = 0;
				frameCountDead++;
				if (frameCountDead == 7)
					killed();
			}
			countDead++;
		} else if (isPanic) { // panic
			if (countPanic == 15) {
				countPanic = 0;
				isPanic = false;
			}
			countPanic++;
			if (direction == 1)
				x += 2;
			else
				x -= 2;
		} else if (isAttack) {
			if (countAttack == 10) {
				countAttack = 0;
				frameCountAttack++;
				if (frameCountAttack == 9) {
					frameCountAttack = 0;
					isAttack = false;
				}
			}
			countAttack++;
		} else { // walk
			if (countWalk == 10) {
				countWalk = 0;
				frameCountWalk++;
				frameCountWalk %= 4;
			}
			countWalk++;
			if (countWalk % 5 == 0) {
				if (x + 50 > hero.getX()) {
					direction = 1;
					x -= (4 - speedDecrease);
				} else if (x + 50 <= hero.getX() && x + 120 >= hero.getX()) {
					isAttack = true;
					attackDirection = direction;
				} else if (x + 120 < hero.getX()) {
					direction = 2;
					x += (4 - speedDecrease);
				}
			}
		}

		if (InputUtility.getKeyPressed(KeyEvent.VK_RIGHT)) {
			if (!(hero.getX() < 400 || land.isEnd()))
				hero.setRight(true);
		}
		if (hero.getX() >= 400 && hero.isRight()) {
			setX(5);
		}
		if (InputUtility.getKeyPressed(KeyEvent.VK_LEFT)) {
			if (!(hero.getX() > 200 || land.isStart())) {
				hero.setLeft(true);
			}
		}
		if (hero.getX() <= 200 && hero.isLeft()) {
			setX(-5);
		}

		// Debuff
		if (debuff[0] != 0) {
			if (countDebuff[0] == 40) {
				countDebuff[0] = 0;
				timeCountDebuff[0]++;
				this.damageFormDebuff(hero, 5);
				if (timeCountDebuff[0] == 3) {
					timeCountDebuff[0] = 0;
					countDebuff[0] = 0;
					debuff[0] = 0;
				}
			}
			countDebuff[0]++;
		}
		if (debuff[1] != 0) {
			if (countDebuff[1] == 40) {
				countDebuff[1] = 0;
				timeCountDebuff[1]++;
				if (timeCountDebuff[1] == 3) {
					speedDecrease = 0;
					timeCountDebuff[1] = 0;
					countDebuff[1] = 0;
					debuff[1] = 0;
				}
			}
			countDebuff[1]++;
		}
		if (debuff[2] != 0) {
			if (countDebuff[2] == 40) {
				countDebuff[2] = 0;
				timeCountDebuff[2]++;
				this.damageFormDebuff(hero, 3);
				if (timeCountDebuff[2] == 3) {
					timeCountDebuff[2] = 0;
					countDebuff[2] = 0;
					debuff[2] = 0;
				}
			}
			countDebuff[2]++;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (hp == 0) { // dead
			if (direction == 1) {
				g.drawImage(Resource.monster_yeti_1_2.getSubimage(
						Resource.monster_yeti_1_2.getWidth() / 7
								* frameCountDead, 0,
						Resource.monster_yeti_1_2.getWidth() / 7,
						Resource.monster_yeti_1_2.getHeight()), null, x, y);
			} else if (direction == 2) {
				g.drawImage(Resource.monster_yeti_1_2_f.getSubimage(
						Resource.monster_yeti_1_2_f.getWidth() / 7
								* (6 - frameCountDead), 0,
						Resource.monster_yeti_1_2_f.getWidth() / 7,
						Resource.monster_yeti_1_2.getHeight()), null, x, y);
			}
		} else if (isPanic) { // panic
			if (direction == 1) {
				g.drawImage(Resource.monster_yeti_1_3.getSubimage(0, 0,
						Resource.monster_yeti_1_3.getWidth(),
						Resource.monster_yeti_1_3.getHeight()), null, x, y);
			} else if (direction == 2) {
				g.drawImage(Resource.monster_yeti_1_3_f.getSubimage(0, 0,
						Resource.monster_yeti_1_3_f.getWidth(),
						Resource.monster_yeti_1_3_f.getHeight()), null, x, y);
			}
		} else if (isAttack) {
			if (attackDirection == 1) {
				g.drawImage(Resource.monster_yeti_1_4.getSubimage(
						Resource.monster_yeti_1_4.getWidth() / 9
								* frameCountAttack, 0,
						Resource.monster_yeti_1_4.getWidth() / 9,
						Resource.monster_yeti_1_4.getHeight()), null, x, y);
			} else if (attackDirection == 2) {
				g.drawImage(Resource.monster_yeti_1_4_f.getSubimage(
						Resource.monster_yeti_1_4_f.getWidth() / 9
								* (8 - frameCountAttack), 0,
						Resource.monster_yeti_1_4_f.getWidth() / 9,
						Resource.monster_yeti_1_4_f.getHeight()), null, x, y);
			}
		} else { // walk
			if (direction == 1) {
				g.drawImage(Resource.monster_yeti_1_1.getSubimage(
						Resource.monster_yeti_1_1.getWidth() / 4
								* frameCountWalk, 0,
						Resource.monster_yeti_1_1.getWidth() / 4,
						Resource.monster_yeti_1_1.getHeight()), null, x, y);
			} else if (direction == 2) {
				g.drawImage(Resource.monster_yeti_1_1_f.getSubimage(
						Resource.monster_yeti_1_1_f.getWidth() / 4
								* (3 - frameCountWalk), 0,
						Resource.monster_yeti_1_1_f.getWidth() / 4,
						Resource.monster_yeti_1_1_f.getHeight()), null, x, y);
			}
		}

		if (debuff[0] != 0) {
			if (direction == 2) {
				g.drawImage(Resource.burn, null, x + 25, y + 90);
			} else {
				g.drawImage(Resource.burn, null, x + 105, y + 90);
			}
		}
		if (debuff[1] != 0) {
			if (direction == 2) {
				g.drawImage(Resource.freeze, null, x + 50, y + 90);
			} else {
				g.drawImage(Resource.freeze, null, x + 130, y + 90);
			}
		}
		if (debuff[2] != 0) {
			if (direction == 2) {
				g.drawImage(Resource.poison, null, x + 75, y + 90);
			} else {
				g.drawImage(Resource.poison, null, x + 155, y + 90);
			}
		}
	}

	public void damageFormDebuff(Hero hero, int damage) {
		hp -= hero.getInt() * damage * type;
		if (hp <= 0) {
			hp = 0;
		}
	}

	public void hit(Hero hero, Skill skill) {
		if (!isPanic) {
			if (skill instanceof FireSkill) {
				damageTaken = hero.getAttack() * 2;
				debuff[0] = 1; // burn
				timeCountDebuff[0] = 0;
				countDebuff[0] = 0;
			} else if (skill instanceof IceSkill) {
				damageTaken = hero.getAttack() * 1;
				if (((int) Math.random() * 4 + 1) == 1) {
					debuff[1] = 2; // slow
					speedDecrease = 3;
					timeCountDebuff[1] = 0;
					countDebuff[1] = 0;
				}
			} else if (skill instanceof PoisonSkill) {
				damageTaken = hero.getAttack() * 1;
				if (((int) Math.random() * 4 + 1) == 1) {
					debuff[2] = 3; // poisoned
					timeCountDebuff[2] = 0;
					countDebuff[2] = 0;
				}
			} else if (skill instanceof MeteorSkill) {
				damageTaken = hero.getAttack() * 4;
				debuff[0] = 1;
				timeCountDebuff[0] = 0;
				countDebuff[0] = 0;
			} else if (skill instanceof SpikeSkill) {
				damageTaken = hero.getAttack() * 2;
			}
			hp -= damageTaken;
			isPanic = true;
			if (hp <= 0) {
				hp = 0;
				isPanic = false;
			}
		}
	}

	@Override
	public boolean isVisible() {
		return !isDead;
	}

	@Override
	public int getZ() {
		return 0;
	}

}
