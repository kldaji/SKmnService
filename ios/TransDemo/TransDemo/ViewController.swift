//
//  ViewController.swift
//  TransDemo
//
//  Created by 장진우 on 2021/02/16.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet weak var urlTextField: UITextField!
    
    @IBOutlet var loadingIndicator: UIStackView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    @IBSegueAction func doneSegue(_ coder: NSCoder) -> DoneViewController? {
        if let text = self.urlTextField.text {
            let result = summarize(url: text)
            return DoneViewController(coder: coder, result: result)
        } else {
            let alert = UIAlertController(title: nil, message: "Please put the video url", preferredStyle: .alert)
            self.present(alert, animated: true, completion: nil)
            return nil
        }
    }
    
}

