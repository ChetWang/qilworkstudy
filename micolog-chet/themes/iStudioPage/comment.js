jQuery(document).ready(
    function(){

loadjs=false;

commentuser=$.cookie('comment_user');
if (commentuser)
{
 //[user,email,url]=commentuser.split('#@#');
 data=commentuser.split('#@#');
 $('#author').val(data[0]);
 $('#email').val(data[1]);
 $('#url').val(data[2]);
};

$('#commentform').ajaxForm({
        type:'post',
        dataType:  'json',
        beforeSubmit:function(formData,jqForm,options){
               var form = jqForm[0];

               if (form.author)
               {

                     if (!form.author.value)
                     {
                      showinfo('Please enter your name!');
                      form.author.focus();
                      return false;
                     }
                     if (!form.email.value)
                     {
                        showinfo('Please enter email address!');
                        form.email.focus();
                        return false;
                     }
               }
                if($('#checkarea').css('display')=='block')
                               {
                                 if(!form.checkret.value)
                                 {
                                   alert('Count error ,please retry it ,3Q!');
                                   form.checkret.focus();
                                   return false;
                                 }

                               }

                 if (!form.comment.value)
                 {
                    showinfo('Please enter a message!');
                    form.comment.focus();
                    return false;
                 }

                 $('#s_msg').text('Submitted message...').show();
                $("#submit").attr('disabled',true);

                 return true;

            },
        success:function(data){
            $("#submit").attr('disabled',false);
              if (data[0])
              {
                /*document.cookie=data[2];*/
                    alert('Submitted successfully!');
                add_comment(data[1]);

                $('#s_msg').text('Submitted successfully!');
                $('#comment').val('');
                if($('#checkarea').css('display')=='block')
                {
                    if($("#check_type").val()>0)
                    {
                        get_check_area($("#check_type").val());
                        reloadCheckImage();
                    }
                }
                $('#checkret').val('');
                location="#comments";
              }
              else
              {  if (data[1]==-102)
                 showinfo('Count error ,please retry it ,3Q!');
                 $('#s_msg').text('Submission failed!');
                 $('#checkret').focus();

              }
            }
        });
    }
)
function get_check_area(type)
{
    if (type==1)
    {
        $('#check').load('/checkcode/');
        $('#checkarea').show();
    }else if(type==2)
    {
        $('#check').html('<img id="checkimg" src="/checkimg/" style="border:0px;padding:0;float:left;margin-right:8px" title="点击图片切换" onclick="reloadCheckImage();"/>');
        $('#checkarea').show();
    }

}

function reloadCheckImage()
{
    var img = document.getElementById('checkimg');
    img.src += "?";
}

function showinfo(msg)
{
  alert(msg);
}
function add_comment(msg)
{
  comment=$(msg)
  if (!loadjs)
  {
    $("#thecomments").prepend(comment).show();
    $.getScript("http://dev.jquery.com/view/trunk/plugins/color/jquery.color.js", function(){
         comment.animate( { backgroundColor: '#fbc7c7' }, "slow")
                    .animate( { backgroundColor: 'white' }, "slow")
                    loadjs=true;
                });
  }else
  {
    $("#thecomments").prepend(comment);
      comment.animate( { backgroundColor: '#fbc7c7' }, "slow")
                    .animate( { backgroundColor: 'white' }, "slow")

  }
}

function backcomment(author,id){
    backdb=document.getElementById('comment');
    backdb.focus();
    backdb.value=backdb.value+'<a href=\"#comment-'+id+'\">@'+author+'<\/a>'+'\n';
    return false;
}
