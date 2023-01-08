package com.mourarezendecas.olxscraper.models

class ItemModel {
    String title
    Double value
    String address
    String adURL


    @Override
    public String toString() {
        return "---ItemModel---" + '\n' +
                "Titulo: " + title + '\n' +
                "Valor: R\$" + value + '\n' +
                "Endereço: " + address + '\n' +
                "URL: " + adURL + '\n'
    }
}
