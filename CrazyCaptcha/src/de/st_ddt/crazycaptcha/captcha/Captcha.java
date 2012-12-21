package de.st_ddt.crazycaptcha.captcha;

import org.bukkit.entity.Player;

public interface Captcha
{

	public void sendRequest(Player player);

	public boolean check(String captcha);
}
