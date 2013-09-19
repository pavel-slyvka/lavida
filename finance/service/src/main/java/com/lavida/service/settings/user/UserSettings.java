package com.lavida.service.settings.user;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * The class for each user's settings of all swing components.
 * Created: 15:20 18.09.13
 *
 * @author Ruslan
 */
@XmlRootElement
public class UserSettings {

    private List<RoleSettings> roleSettings;

}
