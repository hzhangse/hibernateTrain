package com.train.hibernate.event;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;

import com.train.hibernate.entity.annotation.User;

public class SaveUserListener extends DefaultSaveOrUpdateEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6964127086976708335L;

	public void onSaveOrUpdate(SaveOrUpdateEvent event)
			throws HibernateException {
		if (event.getObject() instanceof User) {
			User user = (User) event.getObject();
			System.out.println("find save User:" + user.getName());
		//	user.setDate(new Date());
		}
		super.onSaveOrUpdate(event);
	}
}
