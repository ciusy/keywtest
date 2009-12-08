
$(document).ready(
    function(){
        $('.leftSide').mouseover(function(){
            var url=$(this).css("background-image");
            if(url.indexOf('blank_')!=-1)
                $(this).css('background-image', 'url(../img/blank_over.gif)');
        }).mouseout(function(){
            var url=$(this).css("background-image");
            if(url.indexOf('blank_')!=-1)
                $(this).css('background-image', 'url(../img/blank_seat.gif)');
        });
        $('.rightSide').mouseover(function(){
            var url=$(this).css("background-image");
            if(url.indexOf('blank_')!=-1)
                $(this).css('background-image', 'url(../img/blank_over.gif)');
        }).mouseout(function(){
            var url=$(this).css("background-image");
            if(url.indexOf('blank_')!=-1)
                $(this).css('background-image', 'url(../img/blank_seat.gif)');
        }); 

        $('.leftSide').click(function(){
            //$.get("/keyplot/gogame/web/ajax/match.jsp?method=sitdown",
            //{seatId:$(this).attr("id")} ,
            //     callback);
            var id=$(this).attr("id");
            $.ajax({

                url: "/keyplot/gogame/web/ajax/match.jsp",
                type: 'GET',
                data: "method=sitdown&seatId="+$(this).attr("id"),
                dataType: 'xml',
                error: function(){
                    alert('Error loading XML document');
                },
                success: function(xml){
                    var o=$(this);
                    $(xml).find('value').each(function(){
                        var item_text = $(this).text();
                        if(item_text=="successSitdown"){
                            alert(item_text);
                            alert($('#'+id).attr("id"));
                            $('#'+id).css('background-image', 'url(../img/yiyi.gif)');
                        }                        
                        $('<li></li>')
                        .html(item_text)
                        .appendTo('ol');
                    });
                }
            });        
        });
    }
    );



