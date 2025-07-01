if (document.readyState == 'loading') {
    document.addEventListener('DOMContentLoaded', ready)
} else {
    ready()
}

function ready() {
    var removeCartItemButtons = document.getElementsByClassName('btn-danger')
    for (var i = 0; i < removeCartItemButtons.length; i++) {
        var button = removeCartItemButtons[i]
        button.addEventListener('click', removeCartItem)
    } 

    var addToCartButtons = document.getElementsByClassName('add-button')
    for (var i = 0; i < addToCartButtons.length; i++) {
        var button = addToCartButtons[i]
        button.addEventListener('click', addToCartClicked)
    }

    document.getElementsByClassName('btn-appoi')[0].addEventListener('click', appoiClicked)
   
    
}

const cart = document.getElementsByClassName('badge') 

function appoiClicked() {
    alert('All done!')
    var cartItems = document.getElementsByClassName('cart-items')[0]
    while (cartItems.hasChildNodes()) {
        cartItems.removeChild(cartItems.firstChild)        
    }
    updateCartTotal()
    
    for (var i = 0; i < cart.length; i++) {        
      
        cart[i].innerText = 0
        localStorage.setItem('cart', cart[i].innerText);        
    }    
}


function removeCartItem(event) {
    var buttonClicked = event.target
    buttonClicked.parentElement.parentElement.remove()
    updateCartTotal()

    for (var i = 0; i < cart.length; i++) {        
      
        cart[i].innerText--
        localStorage.setItem('cart', cart[i].innerText);        
    }  

}

function quantityChanged(event) {
    var input = event.target
    if (isNaN(input.value) || input.value <= 0) {
        input.value = 1
    }
    updateCartTotal()
}

function addToCartClicked(event) {
    var button = event.target
    var shopItem = button.parentElement.parentElement

    var gender = shopItem.getElementsByClassName('shop-item-genmder')[0].src 

    var service = shopItem.getElementsByClassName('shop-item-service')[0].innerText
    
    var price = shopItem.getElementsByClassName('shop-item-price')[0].innerText
   
    

    addItemToCart(gender, service, price)       
    updateCartTotal()   
}


function addItemToCart(gender, service, price) {
    var cartRow = document.createElement('div')
    cartRow.classList.add('cart-row')
    var cartItems = document.getElementsByClassName('cart-items')[0]    
    cartItemNames = cartItems.getElementsByClassName('cart-item-title')
    for (var i = 0; i < cartItemNames.length; i++) {
        if (cartItemNames[i].innerText == title) {
            alert('This item is already added to the cart')              
            return
        }
    }
    var cartRowContents = `       
                <div class="row">
                <div class="col-3 mt-3">
                <span>Male</span>
                </div>
                <div class="col-5 mt-3">
                <span>Haircut</span>
                </div>
                <div class="col-3 mt-3">
                <span>20</span>
                </div>
                <div class="col-1 mt-3">
                <button type="button" class="btn btn-outline-danger btn-sm"><i class="bi bi-x"></i></button>  
            </div>`
    cartRow.innerHTML = cartRowContents
    cartItems.append(cartRow)
    
    cartRow.getElementsByClassName('btn-danger')[0].addEventListener('click', removeCartItem)
    cartRow.getElementsByClassName('cart-quantity-input')[0].addEventListener('change', quantityChanged)

    for (var i = 0; i < cart.length; i++) {        
        cart[i].innerText++     
        localStorage.setItem('cart', cart[i].innerText);     
    }
 
}
function updateCartTotal() {
    var cartItemContainer = document.getElementsByClassName('cart-items')[0]
    var cartRows = cartItemContainer.getElementsByClassName('cart-row')
    var total = 0
    for (var i = 0; i < cartRows.length; i++) {
        var cartRow = cartRows[i]
        var priceElement = cartRow.getElementsByClassName('cart-price')[0]
        var quantityElement = cartRow.getElementsByClassName('cart-quantity-input')[0]
        var price = parseFloat(priceElement.innerText.replace('$', ''))
        var quantity = quantityElement.value
        total = total + (price * quantity)
    }
    total = Math.round(total * 100) / 100
    document.getElementsByClassName('cart-total-price')[0].innerText = '$' + total
}


