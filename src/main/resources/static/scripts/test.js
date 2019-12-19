var DownLoadOrders = function (url, data) {
    // var config = $.extend(true, { method: 'post' }, options);
    var $iframe = $('<iframe id="down-file-iframe" />');
    var $form = $('<form target="down-file-iframe" method="post" />');
    $form.attr('action', url);
    for (var i=0;i <data.length;i++) {
        $form.append('<input type="hidden" name="orderList" value="' + data[i] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
}
function batchExportUseTmpFile(url) {
    var orderList = [];
    /*$('#ordersTable').find('.tdChoice').each(function(ele){
        if($(this).attr('data-value') != undefined){
            orderList.push($(this).attr('data-value'));
        }
    });*/
    orderList.push("1120181000015871");
    if (orderList === undefined || orderList.length == 0) {
        showMessage({message: '未选择订单'});
        return;
    }
    DownLoadOrders(url, orderList);
}
