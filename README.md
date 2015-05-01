# mercado-bitcoin

A Clojure library designed to use the mercadobitcoin.com.br API for
trading Bitcoins.

The library abstract all the plumbing necessary to correctly interact
with the API. The returned value is the JSON parsed into a Clojure
map, where the dates are clj-time objects.

There is the core namespace which is responsible for the basics of
communication and then the 'btc' and 'ltc' namespaces, which
specialize the communication for the specified coins.

## Usage

There are basically two types of access to the API: 
1) obtaining information 
2) manipulating the trade orders

### Obtaining information
* Retrieve the current exchange information

```
mercado-bitcoin.btc> (ticker)
;=> {:ticker {:high 750.0,
              :low 716.06231,
              :vol 134.4016074,
              :last 737.01,
              :buy 737.07674,
              :sell 749.14159,
              :date #<DateTime 2015-05-01T03:01:53.000Z>}}
```

* Retrieve current orderbook
```
mercado-bitcoin.btc> (orderbook)
;=> {:asks [[749.54958 0.14752609] [749.54959 0.48295231] ...],
     :bids [[737.07674 0.21569403] [737.00001 3.71405] ...]}
```

### Manipulating trade orders
First you need to call a trader to be your operator, but passing your credentials

```
mercado-bitcoin.btc> (def trader (get-trader mercado-tapi-codigo mercado-tapi-chave PIN))
;=> #'mercado-bitcoin.btc/trader
```

Than you can send a buy order

```
mercado-bitcoin.btc> (create-order trader :buy 1 0.01)
;=> {:return {1969656 {:status "active",
                       :operations {},
                       :created #<DateTime 2015-05-01T17:53:08.000Z>,
                       :price 1.0,
                       :volume 0.01,
                       :pair "btc_brl",
                       :type "buy"}},
     :success 1}

mercado-bitcoin.btc> (create-order trader :buy 1 0.01)
;=> {:return {1969660 {:status "active",
                       :operations {},
                       :created #<DateTime 2015-05-01T17:53:16.000Z>,
                       :price 1.0,
                       :volume 0.01,
                       :pair "btc_brl",
                       :type "buy"}},
     :success 1}
```

Then retrieve current status for an order id interval
```
mercado-bitcoin.btc> (orders-list-status trader 1969656 1969660)
;=> {:return {1969660 {:status "active",
                      :operations {},
                      :created #<DateTime 2015-05-01T17:53:16.000Z>,
                      :price 1.0,
                      :volume 0.01,
                      :pair "btc_brl",
                      :type "buy"},
             1969656 {:status "active",
                      :operations {},
                      :created #<DateTime 2015-05-01T17:53:08.000Z>,
                      :price 1.0,
                      :volume 0.01,
                      :pair "btc_brl",
                      :type "buy"}},
    :success 1}
```

Or for a single one

```
mercado-bitcoin.btc> (order-status trader 1969656)
;=> {:return {1969656 {:status "active",
                       :operations {},
                       :created #<DateTime 2015-05-01T17:53:08.000Z>,
                       :price 1.0,
                       :volume 0.01,
                       :pair "btc_brl",
                       :type "buy"}},
     :success 1}
```

Or if you changed your mind, cancel them

```
mercado-bitcoin.btc> (cancel-order trader 1969656)
;=> {:return {1969656 {:status "canceled",
                       :operations {},
                       :created #<DateTime 2015-05-01T17:53:08.000Z>,
                       :price 1.0,
                       :volume 0.01,
                       :pair "btc_brl",
                       :type "buy"}},
     :success 1}

mercado-bitcoin.btc> (cancel-order trader 1969660)
;=> {:return {1969660 {:status "canceled",
                       :operations {},
                       :created #<DateTime 2015-05-01T17:53:16.000Z>,
                       :price 1.0,
                       :volume 0.01,
                       :pair "btc_brl",
                       :type "buy"}},
     :success 1}
```
     
How about a sell order?

```
mercado-bitcoin.btc> (create-order trader :sell 10000 0.01)
;=> {:return {1969982 {:status "active",
                       :operations {},
                       :created #<DateTime 2015-05-01T18:11:00.000Z>,
                       :price 10000.0,
                       :volume 0.01,
                       :pair "btc_brl",
                       :type "sell"}},
     :success 1}
                       
mercado-bitcoin.btc> (cancel-order trader 1969982)
;=> {:return {1969982 {:status "canceled",
                       :operations {},
                       :created #<DateTime 2015-05-01T18:11:00.000Z>,
                       :price 10000.0,
                       :volume 0.01,
                       :pair "btc_brl",
                       :type "sell"}},
     :success 1}
```

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
