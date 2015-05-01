# mercado-bitcoin-api

This is a Clojure library designed to connect to the mercadobitcoin.com.br API for
trading Cryptocurrencies (on this date, BTC or LTC).

The library abstract all the plumbing necessary to correctly interact
with the API. The returned value is the JSON parsed into a Clojure
map, as seen below. Moreover, the dates become clj-time objects.

In terms of the library structure, there is the 'core' namespace which is responsible for the basics of
communication and then the 'btc' and 'ltc' namespaces, which
specialize the communication for the specified coins.

## Usage

There are basically two types of access to the API: 

1) obtaining information 
2) manipulating the trade orders

### 1) Obtaining information
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

* Retrieve trades
```
mercado-bitcoin.btc> (trades)
;=> [{:date #<DateTime 2015-04-27T23:41:38.000Z>, :price 689.99999, :amount 0.07246377, :tid 130368, :type "buy"}
     {:date #<DateTime 2015-04-27T23:47:57.000Z>, :price 689.99998, :amount 0.06705797, :tid 130369, :type "buy"}
     ...
    ]
```

### Manipulating trade orders
First you need to call a trader to be your operator, but he won't
listen to you without some credentials:

```
mercado-bitcoin.btc> (def trader (get-trader mercado-tapi-codigo mercado-tapi-chave PIN))
;=> #'mercado-bitcoin.btc/trader
```

Than you can send buy orders

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

Check their current status
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

Or just check the status for a single order

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

If you change your mind, just cancel them

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

There you have it! Remember that there are all the equivalent
functionalities for LTC in the ltc namespace.

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
