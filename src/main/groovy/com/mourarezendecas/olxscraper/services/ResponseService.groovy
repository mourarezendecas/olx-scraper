package com.mourarezendecas.olxscraper.services

import com.mourarezendecas.olxscraper.models.InvalidResponseModel
import com.mourarezendecas.olxscraper.models.ItemModel
import com.mourarezendecas.olxscraper.models.RequestModel
import com.mourarezendecas.olxscraper.models.ResponseModel
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service

@Service
class ResponseService {
    static String mainURL = 'https://www.olx.com.br/'
    static String endpointEstados = 'estado-'
    static String stateAcronym
    static String searchTitle

    static def scrape(RequestModel requestModel){
        searchTitle = requestModel.title
        stateAcronym = requestModel.state
        collect(requestModel)
    }

    static String refineTitle(String title){
        return title.replace(" ","").toUpperCase()
    }

    static Boolean isAdvertisingTitleValid(String adTitle, String searchTitle){
        return adTitle.findAll(searchTitle)
    }

    static def collect(RequestModel requestModel){
        ResponseModel responseModel = new ResponseModel()

        List<ItemModel> itemModelList = []

        for(int pageNo = 1; pageNo<=3; pageNo++){
            Map queryParams = [o:pageNo.toString(), q:requestModel.title.replace(" ","%")]
            Document page = connect(queryParams).parse()

            if(checkAds(page)){
                if(itemModelList.size()==0){
                    return new InvalidResponseModel()
                }
                else{
                    break
                }
            }

            List<Element> adList = page.getElementsByClass('sc-12rk7z2-0 bjnzhV')

            try{
                itemModelList += makeListofItemModel(adList)
            }catch(e)
            {
                e.printStackTrace()
            }

        }
        responseModel.searchTitle = searchTitle

        responseModel.advertisements = itemModelList

        responseModel.adAmount = responseModel.advertisements.size()

        responseModel.avaragePrice = getAveragePrice(responseModel.advertisements).round(2)

        responseModel.moreExpensiveItem = getMoreExpensiveItem(responseModel.advertisements)

        responseModel.cheapestItem = getCheapestItem(responseModel.advertisements, responseModel.moreExpensiveItem)

        return responseModel
    }

    static connect(Map queryParams){
        Connection.Response response = Jsoup.connect(mainURL+endpointEstados+stateAcronym)
                .data(queryParams)
                .method(Connection.Method.GET)
                .execute()
        return response
    }


    static priceToDouble(String value){
        return value.replaceAll(/\D+/,"").toDouble()
    }

    static makeListofItemModel(List<Element> adList){
        List<ItemModel> itemModelList = []

        for(Element advertisement : adList ){
            ItemModel itemModel = new ItemModel()
            String refinedTitle

            try{
                itemModel.title = advertisement.getElementsByTag('h2').first().text()
                refinedTitle = refineTitle(itemModel.title)
            }catch(e)
            {
                e.printStackTrace()
            }

            if(isAdvertisingTitleValid(refinedTitle, refineTitle(searchTitle))) {

                if (advertisement.getElementsByClass('sc-1kn4z61-1 dGMPPn').first().text() == '') {
                    itemModel.value = 0
                } else {
                    try {
                        itemModel.value = priceToDouble(advertisement.getElementsByClass('sc-1kn4z61-1 dGMPPn').first().text())
                    } catch (e) {
                        e.printStackTrace()
                    }
                }

                try {
                    itemModel.address = advertisement.getElementsByClass('sc-1c3ysll-0 lfQETj').first().text()
                } catch (e) {
                    e.printStackTrace()
                }

                try {
                    itemModel.adURL = advertisement.getElementsByTag('a').attr('href')
                } catch (e) {
                    e.printStackTrace()
                }

                try {
                    itemModelList.add(itemModel)
                } catch (e) {
                    e.printStackTrace()
                }
            }

        }

        return itemModelList
    }

    static getAveragePrice(List<ItemModel> itemModelList){
        Double averagePrice = 0
        for(ItemModel itemModel in itemModelList){
            averagePrice += itemModel.value
        }
        return averagePrice/itemModelList.size()
    }


    static getCheapestItem(List<ItemModel> itemModelList, ItemModel moreExpensiveItem){
        ItemModel cheapestItem = new ItemModel(value: moreExpensiveItem.value)

        for(ItemModel itemModel in itemModelList){
            if(itemModel.value!=0 && itemModel.value < cheapestItem.value){
                cheapestItem = itemModel
            }
        }
        return cheapestItem
    }

    static getMoreExpensiveItem(List<ItemModel> itemModelList){
        ItemModel moreExpensiveItem = new ItemModel(value: 0)

        for(ItemModel itemModel in itemModelList){
            if(itemModel.value>moreExpensiveItem.value){
                moreExpensiveItem = itemModel
            }
        }
        return moreExpensiveItem
    }

    static checkAds(Document page){
        if(page.getElementsByClass('sc-145t6x-0 cxrWpf')){
            return true
        }
    }
}
