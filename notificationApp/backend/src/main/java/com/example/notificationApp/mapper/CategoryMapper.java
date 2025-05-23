package com.example.notificationApp.mapper;

import java.util.Locale;

public class CategoryMapper {

    public static String mapCategoryByName(String productCategory){
        if (productCategory == null) return "Diğer";
        String lowerName = productCategory.toLowerCase(Locale.ROOT);

        // Fashion
        if ((lowerName.contains("ayakkabı")) || lowerName.contains("sneaker")) return "Ayakkabı";
        if (lowerName.contains("tişört")) return "Tişört";
        if (lowerName.contains("t-shirt")) return "Tişört";
        if (lowerName.contains("mont")) return "Mont";
        if (lowerName.contains("pantolon")) return "Pantolon";
        if (lowerName.contains("pijama")) return "Pijama";
        if (lowerName.contains("kazak")) return "Kazak";
        if (lowerName.contains("yelek")) return "Yelek";
        if (lowerName.contains("ceket")) return "Ceket";
        if (lowerName.contains("gömlek")) return "Gömlek";
        if (lowerName.contains("çorap")) return "Çorap";
        if (lowerName.contains("çanta")) return "Çanta";

        // Electronics
        if (lowerName.contains("telefon")) return "Telefon";
        if (lowerName.contains("laptop")||lowerName.contains("bilgisayar")) return "Bilgisayar";
        if (lowerName.contains("tablet")) return "Tablet";
        if (lowerName.contains("kulaklık")) return "Kulaklık";
        if (lowerName.contains("saat")) return "Akıllı Saat";
        if ((lowerName.contains("kamera"))||lowerName.contains("fotoğraf")||lowerName.contains("video")) return "Kamera";
        if (lowerName.contains("televizyon")) return "Televizyon";
        if (lowerName.contains("hoparlör")) return "Hoparlör";
        if (lowerName.contains("buzdolabı")) return "Buzdolabı";
        if (lowerName.contains("çamaşır")) return "Çamaşır Makinesi";
        if (lowerName.contains("bulaşık")) return "Bulaşık Makinesi";
        if (lowerName.contains("ütü")) return "Ütü";
        if ((lowerName.contains("konsol"))||lowerName.contains("playstation")||lowerName.contains("xbox")||lowerName.contains("nintendo")) return "Oyun Konsolu";

        // Hobby
        if (lowerName.contains("drone")) return "Drone";
        if (lowerName.contains("bisiklet")) return "Bisiklet";
        if (lowerName.contains("scooter")) return "Scooter";
        if (lowerName.contains("top")) return "Top";
        if (lowerName.contains("raket")) return "Raket";
        if (lowerName.contains("kitap")) return "Kitap";
        if (lowerName.contains("kaykay")) return "Kaykay";
        if (lowerName.contains("paten")) return "Paten";
        if (lowerName.contains("gitar")) return "Gitar";

        // Cosmetics
        if (lowerName.contains("parfüm")) return "Parfüm";
        if (
                lowerName.contains("makyaj") ||
                        lowerName.contains("maskara") ||
                        lowerName.contains("allık") ||
                        lowerName.contains("kapatıcı") ||
                        lowerName.contains("rimel") ||  // varsa senin özel tanımın
                        lowerName.contains("fondöten") ||
                        lowerName.contains("dudak kalemi") ||
                        lowerName.contains("göz makyajı") ||
                        lowerName.contains("ten makyajı") ||
                        lowerName.contains("makyaj seti") ||
                        lowerName.contains("oje") ||
                        lowerName.contains("aseton") ||
                        lowerName.contains("eyeliner") ||
                        lowerName.contains("göz kalemi") ||
                        lowerName.contains("highlighter") ||
                        lowerName.contains("bb krem") ||
                        lowerName.contains("cc krem") ||
                        lowerName.contains("kontür") ||
                        lowerName.contains("palet") ||
                        lowerName.contains("bronz") ||
                        lowerName.contains("pudra") ||
                        lowerName.contains("takma tırnak") ||
                        lowerName.contains("far")
        ) {
            return "Makyaj";
        }
        if (lowerName.contains("duş jeli")) return "Duj Jeli";
        if (lowerName.contains("şampuan")) return "Şampuan";
        if (lowerName.contains("deodorant")) return "Deodorant";
        if (lowerName.contains("krem")||lowerName.contains("nemlendirici")||lowerName.contains("yüz bakım")) return "Krem";
        if (lowerName.contains("diş macunu")) return "Diş Macunu";
        if (lowerName.contains("diş fırçası")) return "Diş Fırçası";
        if (lowerName.contains("ağda")) return "Ağda";
        if (lowerName.contains("ruj")) return "Ruj";
        // Household
        if (lowerName.contains("ayna")) return "Ayna";
        if (lowerName.contains("koltuk")) return "Koltuk";
        if (lowerName.contains("tava")) return "Tava";
        if (lowerName.contains("tabak") || lowerName.contains("taba")) return "Tabak";
        if (lowerName.contains("çatal")||(lowerName.contains("kaşık"))||(lowerName.contains("bıçak"))) return "Çatal Kaşık Bıçak";
        if (lowerName.contains("yastık")) return "Yastık";
        if (lowerName.contains("avize")) return "Avize";
        if (lowerName.contains("yemek takımı")) return "Yemek Takımı";
        if (lowerName.contains("yorgan")) return "Yorgan";

        return "Diğer";
    }




}
