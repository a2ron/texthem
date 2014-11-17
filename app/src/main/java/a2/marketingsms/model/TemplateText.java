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

package a2.marketingsms.model;


public class TemplateText {

    private static char[] patterns = {'Á', 'É', 'Í', 'Ó', 'Ú', 'á', 'é', 'í', 'ó', 'ú'};
    private static char[] replacement = {'A', 'E', 'I', 'O', 'U', 'a', 'e', 'i', 'o', 'u'};

    public static String normalice(String t) {
        /*replace for charset gsm instead of unicode*/
        for (int i = 0; i < patterns.length; ++i)
            t = t.replace(patterns[i], replacement[i]);
        return t;
    }

    private String text;

    public TemplateText(String text) {

        this.text = normalice(text);
    }

    /**
     * Replace "wildcards" for Contact values
     *
     * @param c contact to get the values
     * @return the text result
     */
    public String getInstance(Contact c) {
        String res = text;
        if (res != null) {
            res = res.replaceAll("#nombre", normalice(c.getName()));
            res = res.replaceAll("#apellidos", normalice(c.getSurname()));
        }
        return res;
    }

    @Override
    public String toString() {
        return text;
    }
}
