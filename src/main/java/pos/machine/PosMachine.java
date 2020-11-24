package pos.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        int totalPrice = 0;
        String receipt = "***<store earning no money>Receipt***\n";
        List<ItemInfo> itemInfo = ItemDataLoader.loadAllItemInfos();
        List<String> itemList = ItemDataLoader.loadBarcodes();
        List<String> shortlistedBarcodes = barcodes.stream().distinct().collect(Collectors.toList());

        for (int currentBarcode = 0; currentBarcode < shortlistedBarcodes.size(); currentBarcode++){
            ProductDetails productDetails = getData(shortlistedBarcodes.get(currentBarcode),itemInfo);
            int count = getCount(productDetails,itemList);
            productDetails.setCount(count);

            int subtotal = calculateSubtotal(productDetails.getCount(),productDetails.getPrice());
            productDetails.setSubtotal(subtotal);

            totalPrice = totalPrice + subtotal;
            receipt += appendProductDetails(productDetails.getName(), productDetails.getCount(), productDetails.getPrice(), productDetails.getSubtotal());
        }
        receipt += "----------------------\n";
        receipt += "Total: "+totalPrice+" (yuan)\n";
        receipt += "**********************";

        return receipt;
    }

    private ProductDetails getData(String barcodeList, List<ItemInfo> itemInfo){
        for (int barcodeNum = 0; barcodeNum < itemInfo.size(); barcodeNum++){
            if(itemInfo.get(barcodeNum).getBarcode()==barcodeList){
                ProductDetails currentProduct = new ProductDetails();
                currentProduct.setPrice(itemInfo.get(barcodeNum).getPrice());
                currentProduct.setName(itemInfo.get(barcodeNum).getName());
                currentProduct.setBarcode(itemInfo.get(barcodeNum).getBarcode());
                return currentProduct;
            }
        }
        return null;
    }

    private int getCount(ProductDetails products, List<String> itemList) {
        int count = 0;
        for (int barcodeNum = 0; barcodeNum < itemList.size(); barcodeNum++){
            if(itemList.get(barcodeNum)==products.getBarcode()){
                count++;
            }
        }
        return count;
    }

    private String appendProductDetails(String name, int count, int price, int subtotal) {
        String details = "Name: "+name+", Quantity: "+count+", Unit price: "+price+" (yuan), Subtotal: "+subtotal+" (yuan)\n";
        return details;
    }

    private int calculateSubtotal(int count, int price) {
        int subtotal = 0;
        subtotal = count * price;
        return subtotal;
    }
}
