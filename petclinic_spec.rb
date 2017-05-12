require 'headless'
require 'selenium-webdriver'
require 'minitest/spec'
require 'minitest/autorun'

describe 'Petlinic' do
  before do
    @headless = Headless.new
    @headless.start

    @driver = Selenium::WebDriver.for :firefox
    @driver.navigate.to 'http://dashboard:8080/petclinic'
    @wait = Selenium::WebDriver::Wait.new(:timeout => 3)
  end

  after do
    @headless.destroy
  end

  describe 'when homepage is available' do
    it 'I should see Page title' do
      puts "Title is: #{@driver.title}"
      assert @driver.title == "PetClinic :: a Spring Framework demonstration"
    end
  end


  # describe 'when homepage is available' do
  #   it 'click the find owners button' do
  #       @driver.find_element(:class, "icon-search").click
  #       form = @driver.find_element(:id, "lastName")
  #       puts "form is  #{form.label}"
  #     assert @driver.title == "PetClinic :: a Spring Framework demonstration"
  #   end
  # end

end
