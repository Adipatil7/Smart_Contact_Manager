console.log("this is script file")

const toggleSidebar=()=>{
    if($('.sidebar').is(":visible")){
        //band karna hai
        $('.sidebar').css("display","none");
        $('.content').css("margin-left", "0%");
    }else{
        //show karna hai
        $('.sidebar').css("display","block");
        $('.content').css("margin-left", "20%");
    }
};

const search=()=> {

    let query = $("#search-input").val();
    

    if(query==""){
        $(".search-result").hide();
    }else{
        console.log(query);

        //sending request to server

        let url = `http://localhost:8282/search/${query}`;
        fetch(url).then((response) => {
            return response.json();
        }).then((data) => {
            //console.log(data);
            
            let text = `<div class='list-group'>`;

            data.forEach(contact => {
                text += `<a href='/user/${contact.cid}/contact' class=' text-center list-group-item list-group-item-action'>${contact.name}</a>`;
            });

            text+=`</div>`;

            $(".search-result").html(text);
            $(".search-result").show();

        });
    }

};

