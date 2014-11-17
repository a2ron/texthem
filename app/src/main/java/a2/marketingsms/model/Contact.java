package a2.marketingsms.model;

import com.orm.SugarRecord;

/*
Texthem: Massive SMS Sender for Android

Copyright (C) 2014  Aarón Rosas Rodríguez aarr90@gmail.com

This file is part of Texthem.

Texthem is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Texthem is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Texthem.  If not, see <http://www.gnu.org/licenses/>.

*/

public class Contact extends SugarRecord<Contact> {
    String name;
    String surname;
    String phone;

    public Contact()
    {

    }

    public Contact(String name, String surname, String phone) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public boolean checkPhone()
    {
        boolean ok = false;
        if(phone.length()==9)
        {
            char first = phone.charAt(0);
            ok = first=='6' || first=='7';
        }
        return ok;
    }

}
